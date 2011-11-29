def statsMap = [:]

def labels = []
def labelsNum = []
def labelsSingle = []

new File("documents/todo.txt").eachLine{ String line ->	
	def labelsStr =line.substring( line.indexOf("#") ).toLowerCase()
	labels.add( labelsStr ) 
	println labelsStr.split(" ")
	labelsSingle.addAll( labelsStr.split(" ") )
	labelsNum.add( labelsStr.split(" ").size() )
	
}



println "******************************"
println "* Data set size:"
println "******************************"
println "\t" + labels.size()

println "******************************"
println "* Labels and theire frequency:"
println "******************************"
labelsSingle.groupBy { it }.collect{ "${it.key}:${it.value.size}" }.each {
	println "\t${it}"
}

println "******************************"
println "* Labels combinations and theire frequency:"
println "******************************"
labels.groupBy { it }.collect{ "${it.key}:${it.value.size}" }.each {
	println "\t${it}"	
}

println "******************************"
println "* Distribution of # lables:"
println "******************************"
labelsNum.groupBy { it }.collect{ "${it.key} labels: ${it.value.size}" }.each {
	println "\t${it}"
}

println "******************************"
println "* Avg # of labels:"
println "******************************"
println ( "\t" + ( labelsNum.inject(0) { x, y -> x + y } / labels.size() ) )
