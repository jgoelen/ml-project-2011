import sc.Task

class BootStrap {

    def init = { servletContext ->
        new Task(description: 'Task 1').save()
        new Task(description: 'Task 2').save()
     }
     
    def destroy = {
    }
}
