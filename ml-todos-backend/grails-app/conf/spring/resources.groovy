// Place your Spring DSL code here
beans = {
	dataProvider(sc.DataProvider){}
	recommender(sc.DefaultRecommenderImpl){dataProvider=dataProvider}
}
