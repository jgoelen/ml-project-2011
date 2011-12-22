package sc

import mulan.classifier.transformation.BinaryRelevance;
import mulan.data.MultiLabelInstances;
import weka.classifiers.functions.SMO;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances
import weka.core.SparseInstance;
import weka.filters.unsupervised.attribute.StringToWordVector
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Attribute;
import org.apache.commons.logging.LogFactory
import weka.classifiers.Classifier
import weka.classifiers.meta.Vote;
import weka.classifiers.bayes.NaiveBayes;



public interface Recommender {

	List labelsFor(String text)

}

/*This class implements our multi-label classifier*/

public class DefaultRecommenderImpl implements Recommender {

	private static final log = LogFactory.getLog(this)
	
	/*Mulan classifier*/
	BinaryRelevance classifier
	
	/*Provides the traing data and utils to convert strings to vectors*/
	DataProvider dataProvider
	
	boolean trained = false

	public DefaultRecommenderImpl(){
		Vote vote = new Vote();
		vote.setClassifiers( [new NaiveBayes(), new SMO()] as Classifier[] )
		classifier = new BinaryRelevance( vote )
	}
	
	
	/*This will be called from BootStrap.groovy*/
	public void trainClassifier(){
		log.info "Start training classifier"
		def trainingData = dataProvider.loadMultiLabelTrainingInstances()
		classifier.build( trainingData )
		log.info "Finished training classifier"
	}

	
	/*predict the labels for the specified text*/
	public def predict(String text){
	
		log.info "start recommendation for text:$text"
		
		Instance v = dataProvider.makeInstance( text )
		
		log.debug "instance $v"

		def p = classifier.makePrediction( v )

		log.debug "Predicted: $p"
		
		log.info "finished recommendation"
		
		return [prediction:p,vec:v]
		
	}
	
	public List labelsFor(String text){

		def labels = []
		
		def result = predict( text )
		
		def confidences = result.prediction.getConfidences()
		
		result.prediction.getBipartition().eachWithIndex { flag, index -> 
			 	
				def lb = result.vec.attribute(index).name() 	
				labels.addAll([label:lb,recommended:flag,confidence:confidences[index]])
		}
		
		log.debug "Recommend: $labels"
		
		return labels
	}

}