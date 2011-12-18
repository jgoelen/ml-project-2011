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

public interface Recommender {

	List<Label> labelsFor(String text)

}


public class DefaultRecommenderImpl implements Recommender {

	BinaryRelevance classifier = new BinaryRelevance( new SMO() )
	DataProvider dataProvider
	boolean trained = false

	public DefaultRecommenderImpl(){
	}

	public void trainClassifier(){
		println "Start training classifier"
		def trainingData = dataProvider.loadMultiLabelTrainingInstances()
		classifier.build( trainingData )
		println "Finished training classifier"
	}


	public List<Label> labelsFor(String text){
		if(!trained){
			trainClassifier()
			trained = true
		}
		Instance v = dataProvider.makeInstance( text )
		
		println "instance $v"

		def prediction = classifier.makePrediction( v )

		println "Predicted: $prediction"
		
		List<Label> labels = []
		
		prediction.getBipartition().eachWithIndex { flag, index -> 
			if(flag) 
				labels.addAll(Label.findByKey(v.attribute(index).name()))
		}
		return labels
	}

}