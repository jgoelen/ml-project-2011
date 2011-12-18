import sc.Task
import sc.Label

class BootStrap {

    def init = { servletContext ->

    	InputStream stream = servletContext.getResourceAsStream("/WEB-INF/data/ml-todo.xml")
    	def xmlLabels = new XmlParser().parse( stream )
    	
    	xmlLabels.label.each{ l ->
    	
    		def key = l.@name
    		def name  = key.replaceAll("l_","")
    		def label = new Label(key:key,title:name)
    		label.save()
    		println "created $label, key:${label.key}, title:${label.title}"
    	}
    
        new Task(description: 'Task 1').save()
        new Task(description: 'Task 2').save()
     }
     
    def destroy = {
    }
}
