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


public class DefaultRecommenderImpl implements Recommender {

	private static final log = LogFactory.getLog(this)
	
	BinaryRelevance classifier = new BinaryRelevance( new SMO() )
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


	public List labelsFor(String text){
		
		log.info "start recommendation for text:$text"
		
		Instance v = dataProvider.makeInstance( text )
		
		log.debug "instance $v"

		def prediction = classifier.makePrediction( v )

		log.info "Predicted: $prediction"
		
		def labels = []
		
		
		def confidences = prediction.getConfidences()
		
		prediction.getBipartition().eachWithIndex { flag, index -> 
			 	
				def lb = Label.findByKey(v.attribute(index).name()) 	
				labels.addAll([label:lb,recommended:flag,confidence:confidences[index]])
		}
		
		log.info "Recommend: $labels"
		
		return labels
	}

}