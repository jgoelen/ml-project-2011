package be.kuleuven.dtai.ml


import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.supportVector.*
import weka.classifiers.multilabel.*
import weka.classifiers.multilabel.meta.FastBaggingML;
import weka.core.Store;

def f = new File("meka_arff/meka_vectorized_todo.arff")


println f.getAbsolutePath()
//weka.classifiers.functions.SMO -C 12.0 -L 0.0010 -P 1.0E-12 -N 0 -V -1 -W 1 -K "weka.classifiers.functions.supportVector.RBFKernel -C 250007 -G 0.01"
//BR.main(["-t", "${f.getAbsolutePath()}", "-D", "-f", "results.meka", "-W", "weka.classifiers.functions.SMO", "-D", "--", "-C", "12.0", "-L", "0.0010", "-P", "1.0E-12", "-N","0", "-V", "-1", "-W", "1", "-K", 
//									"weka.classifiers.functions.supportVector.RBFKernel -C 250007 -G 0.01"] as String[])


FastBaggingML.main(["_I","50","-D","-t", "${f.getAbsolutePath()}", "-W","weka.classifiers.multilabel.CC", 
					"--", "-D", "-W", "weka.classifiers.functions.SMO"] as String[])
					
					//, "-D", "--", "-C", "12.0", "-L", "0.0010", "-P", "1.0E-12", "-N","0", "-V", "-1", "-W", "1", "-K", 
					//"weka.classifiers.functions.supportVector.RBFKernel -C 250007 -G 0.01"] as String[])
//java weka.classifiers.multilabel.meta.FastBaggingML -I 50 -D -t ENRON-F.arff -W weka.classifiers.multilabel.CC -- -D -W weka.classifiers.functions.SMO
//BR.main(["-?"] as String[])

Store.main(["results.meka", "C" ] as String[])

Store.main(["results.meka", "P" ] as String[])