package jzy.spark.tellu.wizard;

 class DefenseWizard private constructor(){
     companion object{
         var defensewizard = "DefenseWizard_Take_Over"
         var magic = "magic"
         val instance by lazy {
             DefenseWizard()
         }
     }

     init {
//         val intentFilter = IntentFilter()
//         intentFilter.addAction(defensewizard)
//         LocalBroadcastManager.getInstance(GlobalApplicationHolder.getAppContext())
//             .registerReceiver(object : BroadcastReceiver() {
//                 override fun onReceive(context: Context?, intent: Intent) {
//                     val action = intent.action
//                     if (defensewizard.equals(action)) {
//                         conuterAttack(intent.getStringExtra(magic))
//                     }
//                 }
//             }, intentFilter)
     }

     fun conuterAttack(emoJson:String?){
         //1 如果typeid存在就更新情感 设置曝光
         //2 如果typeid不存在直接写入数据库 默认曝光
     }
 }
