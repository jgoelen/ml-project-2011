import sc.Task
import sc.Label

class BootStrap {

    def init = { servletContext ->
    
    	def labels = [ new Label(key:'f_label1',title:'Label 1'), 
    				   new Label(key:'f_label2',title:'Label 2'),
    				   new Label(key:'f_label3',title:'Label 3')]
    	labels*.save()
    
        new Task(description: 'Task 1').save()
        new Task(description: 'Task 2', labels: labels).save()
     }
     
    def destroy = {
    }
}
