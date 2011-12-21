package sc

class LabelController {

	Recommender recommender //DI by convention

    def index = { }
    
    def recommend = { 
    	def text = params["text"]
    	def labels = recommender.labelsFor(text)
    	render(contentType: "text/json") {
            content = array {
                labels.each{ label( [id:it.id, key:it.key, title:it.title ] ) }
            }
        }
    
    }
    
    def recommendAll = {
    	
    	def text = params["text"]
    	
		def r = recommender.labelsFor(text)
    	
    	render(contentType: "text/json") {
            content = array {
                r.each{ label( [id:it.label.id, key:it.label.key, title:it.label.title, recommend: it.recommended, confidence:it.confidence ] ) }
            }
        }

    }
    
}
