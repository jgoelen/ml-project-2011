package sc

public interface Recommender {

	List<Label> labelsFor(String text)

}


public class DefaultRecommenderImpl implements Recommender {

	public List<Label> labelsFor(String text){
		println "labelsFor:" + text	
		Label.list()
	}

}