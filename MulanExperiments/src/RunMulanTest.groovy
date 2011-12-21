import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.RBFKernel;
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


//PatternRecognition07MLkNN.main(["-path","mulan_arff/","-filestem","mulan_todo_terms"] as String[])

//changed name of c
MultiLabelInstances dataset = new MultiLabelInstances("mulan_arff/mulan_todo_terms.arff", 
												"mulan_arff/mulan_todo_terms.xml");

											
println "DATA SET STATS"											
						

Statistics stats = new Statistics()
stats.calculateStats(dataset)					
println stats.toString()									
											
BRkNN learner1 = new BRkNN(9,ExtensionType.EXTA);
MLkNN learner2 = new MLkNN();
/*
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
*

/*
 You can use the method setStrict of the Evaluator class, to force 
non-strict evaluation for all measures.

You could also call crossValidate with your own list of measures, which 
can be initialized as you wish (strict/non-strict).

If all labels are false in an example, then indeed the calculation of 
recall could skip these examples, but this is against its definition, 
which is inspired from information retrieval, where existence of at 
least one relevant document is typically assumed. We believe it is 
better to allow NaN to happen, just to highlight the limitations of 
these measures in multi-label learning. There is always the solution of 
setting strict to false, to bypass this problem.

*/