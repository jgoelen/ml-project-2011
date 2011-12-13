package sc

public interface Recommender {

	List<Label> labelsFor(String text)

}


public class DefaultRecommenderImpl implements Recommender {

	public List<Label> labelsFor(String text){
		println "labelsFor:" + text	
		[1,2,3].collect{ new Label([title:"Label ${it}"]) }
	}

}