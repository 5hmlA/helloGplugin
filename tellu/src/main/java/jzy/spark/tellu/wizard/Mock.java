package jzy.spark.tellu.wizard;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Mock {

    private final static class MockView {

        private onMockClickListener mOnClickListener;

        public void setOnClickListener(onMockClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        public void callOnClick(Object o) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(o);
            }
        }

        interface onMockClickListener {
            void onClick(Object o);
        }
    }

    private final static class ClickObservable extends Observable<Object> {

        MockView mockView;

        public ClickObservable(MockView mockView) {
            this.mockView = mockView;
        }

        @Override
        protected void subscribeActual(Observer<? super Object> observer) {
            ClickReceiver clickReceiver = new ClickReceiver(observer);
            observer.onSubscribe(clickReceiver);
            mockView.setOnClickListener(clickReceiver);
        }

        private class ClickReceiver implements MockView.onMockClickListener, Disposable {

            Observer<? super Object> observer;
            boolean isDisposed;

            public ClickReceiver(Observer<? super Object> observer) {
                this.observer = observer;
            }

            @Override
            public void onClick(Object o) {
                if (!isDisposed()) {
                    observer.onNext(System.currentTimeMillis());
                }
            }

            @Override
            public void dispose() {
                isDisposed = true;
            }

            @Override
            public boolean isDisposed() {
                return isDisposed;
            }
        }
    }

    private final MockView mockView = new MockView();
    private final ClickObservable clickObservable = new ClickObservable(mockView);

    public void execute(){
        mockView.callOnClick(System.currentTimeMillis());
    }

    public Observable<Object> listen(){
        return listen(6);
    }

    public Observable<Object> listen(int windowDuration){
        return clickObservable.throttleFirst(windowDuration, TimeUnit.SECONDS)
                //子线程中处理逻辑
                .observeOn(Schedulers.newThread());
    }

    public Observable<Object> obserClick(){
        return clickObservable;
    }
}
