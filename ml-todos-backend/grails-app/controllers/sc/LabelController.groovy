package sc

class LabelController {

	Recommender todoRecommender

    def index = { }
    
    def recommend = { 
    	def text = params["text"]
    	def labels = todoRecommender.labelsFor(text)
    	render(contentType: "text/json") {
            content = array {
                labels.each{ label( [id:it.id, key:it.key, title:it.title ] ) }
            }
        }
    
    }
    
    def recommendAll = {
    	
    	def text = params["text"]
    	
		def r = todoRecommender.labelsFor(text)
		
		//insert label instance iso labelKey
		r.each{ it.label = Label.findByKey( it.label ) }
    	
    	render(contentType: "text/json") {
            content = array {
                r.each{ label( [id:it.label.id, key:it.label.key, title:it.label.title, recommend: it.recommended, confidence:it.confidence ] ) }
            }
        }

    }
    
}
