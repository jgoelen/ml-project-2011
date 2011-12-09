package sc

class LabelController {

    def index = { }
    
    def recommend = { String text ->
    
    	render(contentType: "text/json") {
            content = array {
                [1,2,3].each{ label( [title:"Label ${it}"]  ) }
            }
        }
    
    }
}
