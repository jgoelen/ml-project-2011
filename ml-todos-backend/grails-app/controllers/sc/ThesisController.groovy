package sc

class ThesisController {
	
	static volatile boolean mustTrain = true
	Recommender thesisRecommender
	DataProvider thesisDataProvider

	def recommendAll = {
    	
    	if(mustTrain){
    		thesisRecommender.trainClassifier()
    		mustTrain = false;
    	}
    	
    	def text = params["text"]
    	
		def labels = thesisRecommender.labelsFor(text)
		
		println labels
    	
    	render(contentType: "text/json") {
            content = array {
                labels.findAll{ it.recommended }.each{ label( [key:it.label, confidence:it.confidence] ) }
            }
        }

    }
    
}