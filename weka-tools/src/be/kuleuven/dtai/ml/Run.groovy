package be.kuleuven.dtai.ml

import static Labels.*;

class Run {

	static main(args) {
		
		def tpp = new ArffUtils()
		/*
		todoLabels.each { label ->

			tpp.createDataSetsForLabel( label, "documents/thesis.txt")
			
		}*/
		
		tpp.createMulanDataSets("thesis","documents/thesis.txt")
	}

}
