import weka.classifiers.functions.SMO;
import weka.classifiers.bayes.*
import weka.classifiers.*
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.meta.Vote;
import weka.classifiers.trees.J48;
import mulan.classifier.lazy.BRkNN;
import mulan.classifier.lazy.BRkNN.ExtensionType;
import mulan.classifier.lazy.MLkNN;
import mulan.classifier.meta.RAkEL;
import mulan.classifier.neural.BPMLL;
import mulan.classifier.transformation.BinaryRelevance;
import mulan.classifier.transformation.LabelPowerset;
import mulan.data.MultiLabelInstances;
import mulan.data.Statistics;
import mulan.evaluation.Evaluator;
import mulan.evaluation.MultipleEvaluation;
import mulan.experiments.PatternRecognition07MLkNN;
import weka.filters.unsupervised.attribute.StringToWordVector
import weka.core.converters.ArffLoader;
import weka.core.stemmers.*
import weka.core.*


StringToWordVector filter
filter = new StringToWordVector()
filter.setOutputWordCounts(false);
filter.setWordsToKeep(2000);
filter.setTFTransform(true);
filter.setIDFTransform(true);
filter.setNormalizeDocLength(new SelectedTag(0,StringToWordVector.TAGS_FILTER));
filter.setLowerCaseTokens(true);
filter.setUseStoplist(true);
filter.setStopwords(null); // use default stopword list
filter.setStemmer(new LovinsStemmer());
filter.setMinTermFreq(1);
filter.setAttributeNamePrefix("_");
filter.setAttributeIndices("first");


ArffLoader arffLoader = new ArffLoader()
arffLoader.setFile( new File("mulan_arff/mulan_thesis.arff") )
Instances trainData = arffLoader.getDataSet()

filter.setInputFormat(trainData)

Instances vectorizedData = StringToWordVector.useFilter( trainData, filter );

MultiLabelInstances dataset = new MultiLabelInstances(vectorizedData, 
													  "mulan_arff/mulan_thesis.xml");

											
println "DATA SET STATS"											
						
Statistics stats = new Statistics()
stats.calculateStats(dataset)					
println stats.toString()									
		

Vote vote = new Vote();
vote.setClassifiers( [ new NaiveBayes(), new SMO() ] as Classifier[] )
def learner = new BinaryRelevance( vote )

Evaluator eval = new Evaluator();
eval.setStrict(false)

print eval.crossValidate(learner, dataset, 10);

/*
MLkNN learner2 = new MLkNN();

SMO smo = new SMO()
RBFKernel kernel = new RBFKernel()
kernel.setGamma(0.0125)
smo.setC(20.0)
smo.setKernel( kernel )

BinaryRelevance learner3 = new BinaryRelevance(smo)

Evaluator eval = new Evaluator();
eval.setStrict(false) //true causes NaN
MultipleEvaluation results;

int numFolds = 10;

println "BRkNN results"
results = eval.crossValidate(learner1, dataset, numFolds);
System.out.println(results);
println "MLkNN results"
results = eval.crossValidate(learner2, dataset, numFolds);
System.out.println(results);
println "SVM results RBF"
results = eval.crossValidate(learner3, dataset, numFolds);
System.out.println(results);

println "SVM results default"
results = eval.crossValidate(new BinaryRelevance(new SMO()), dataset, numFolds);
System.out.println(results);


/*
BPMLL neural = new BPMLL()

println "BPMLL results"
results = eval.crossValidate(neural, dataset, numFolds);
System.out.println(results);
*/