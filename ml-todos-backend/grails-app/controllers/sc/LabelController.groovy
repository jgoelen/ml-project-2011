package sc

class LabelController {

	Recommender recommender //DI by convention

    def index = { }
    
    def recommend = { 
    	def text = params["text"]
    	def labels = recommender.labelsFor(text)
    	render(contentType: "text/json") {
            content = array {
                labels.each{ label( [title: it.title ] ) }
            }
        }
    
    }
}
