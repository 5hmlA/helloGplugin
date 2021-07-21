import org.gradle.api.Plugin
import org.gradle.api.Project

class FirstPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
//        project.task('customTask', type: FirstTask)
    }
}