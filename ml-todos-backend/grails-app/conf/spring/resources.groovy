// Place your Spring DSL code here
beans = {
	todoDataProvider(sc.DataProvider){arffPath="/WEB-INF/data/ml-todo.arff";xmlPath="/WEB-INF/data/ml-todo.xml"}
	thesisDataProvider(sc.DataProvider){arffPath="/WEB-INF/data/ml-thesis.arff";xmlPath="/WEB-INF/data/ml-thesis.xml"}
	todoRecommender(sc.DefaultRecommenderImpl){dataProvider=todoDataProvider}
	thesisRecommender(sc.DefaultRecommenderImpl){dataProvider=thesisDataProvider}
}
