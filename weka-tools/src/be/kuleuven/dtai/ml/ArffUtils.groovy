package be.kuleuven.dtai.ml

class ArffUtils {
	
	final int trainingSize = 66
	final int testSize = 100 - trainingSize
	
	
	public void createMulanDataSets(String datasetName, String textPath){
		
		def lines = new File( textPath ).readLines()
		
		def sortedLabels = Labels.todoLabels.sort()
		
		def labelAttributes = sortedLabels.collect{ lb -> "@attribute ${lb} {0,1}" }.join("\n")
		
		def arffHeader =	"@relation text\n"+
							"@attribute docs string\n"+
							labelAttributes +
							"\n\n"+
							"@data\n"
		
		File arff = new File("mulan_arff/mulan_${datasetName}.arff")
		
		arff.withPrintWriter { p ->
			
			p.write(arffHeader)
			
			lines.each { line ->
				
				String bitVector = sortedLabels.collect{ lb ->
						line.contains("#${lb}") ? "1" : "0" 
				}.join(",")
					
				String cleanedLine = filterLabels(line)
						
				p.write("'${cleanedLine}',${bitVector}\n")
				
			}
		}
		
		new File("mulan_arff/mulan_${datasetName}.xml").withPrintWriter { p ->
			
			p.write("<labels xmlns='http://mulan.sourceforge.net/labels'>\n")
			
			sortedLabels.each{ lb -> p.write("<label name='${lb}'></label>\n")}
			
			p.write("</labels>\n")
		
		}
		
	}
	
	public void createDataSetsForLabel(String label,String textPath){
		
		def lines = new File( textPath ).readLines()
		
		
		def arffHeader =	"@relation text\n"+
							"@attribute docs string\n"+
							"@attribute class {${label},no_${label}}\n\n"+
							"@data\n"
					
		File arff = new File("arff_documents/${label}_string.arff")
		
		arff.withPrintWriter { p ->
			
			p.write(arffHeader)
			
			lines.each { line ->
				
				String clss = line.contains("#${label}" ) ? label : "no_${label}"
				
				String cleanedLine = filterLabels(line)
						
				p.write("'${cleanedLine}',${clss}\n")
				
			}
		}
		
		
							
		
		
	}
	
	def filterLabels = { String line -> 
		def str = line.split("#")[0].trim()
		str.replace("'", " ") 
	}
}
