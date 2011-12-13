package sc

import grails.converters.JSON
 
class TaskController {
 
    def task2map = {t ->
        [guid: "/todos-backend/tasks/$t.id",
         id: t.id,
         description: t.description,
         order: t.order ?: 0,
         isDone: t.isDone]
    }
 
    def list = {
        def tasks = Task.list()
 
        render(contentType: "text/json") {
            content = array {
                tasks.each {task(task2map(it))}
            }
        }
    }
 
    def show = {
        if (params.id) {
            def task = Task.get(params.id)
 
            if (task) {
                render(contentType: "text/json") {
                    content(task2map(task))
                }
            }
            else {
                render text: "${params.id} not found.", status: 404
            }
        }
        else {
            list()
        }
    }
 
    def delete = {
        def task = Task.get(params.id)
 
        if (task) {
            task.delete()
            render "" //would normally return 204 No Content here, but sc-server barfs on 0 bytes.
        }
        else {
            render text: "Task ${params.id} not found.", status: 404
        }
    }
 
    def save = {id=null ->   
    	
    	def text = request.reader.text    
        def payload = JSON.parse(text)
        def task = id ? Task.get(id) : new Task()
 
 		println "Save task: ${payload}"
 
        if (task) {
            task.properties = payload
            if (task.save()) {
                render(contentType: "text/json") {
            		content(task2map(task)) 
        		}
            }
            else {
                render text: "Task could not be saved.", status: 500
            }
        }
        else {
            render text: "Task ${params.id} not found.", status: 404
        }
    }
 
    def update = {
        save(params.id)
    }
}

