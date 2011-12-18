package sc

import org.springframework.web.context.*
import javax.servlet.*
import mulan.classifier.transformation.BinaryRelevance;
import mulan.data.*;
import weka.classifiers.functions.SMO;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances
import weka.core.SparseInstance;
import weka.filters.unsupervised.attribute.StringToWordVector
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Attribute;
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware


public class DataProvider implements ServletContextAware {

	StringToWordVector filter = new StringToWordVector()
	def dataTemplate 
	def servletContext
	
	private InputStream loadFile(def path){
		return servletContext.getResourceAsStream( path )
	}
	
	
	private Instances loadStringTrainingData(){
		ArffLoader arffLoader = new ArffLoader()
		arffLoader.setSource( loadFile("/WEB-INF/data/ml-todo.arff")  )
		return arffLoader.getDataSet()
	}

	
	Instances vectorizeData(Instances data){
		dataTemplate = data.stringFreeStructure()
		filter.setInputFormat( data )
		def vectorized = StringToWordVector.useFilter( data, filter )
		return vectorized
	}

	
	public MultiLabelInstances loadMultiLabelTrainingInstances(){
		def labelNodes = Label.list().collect{new LabelNodeImpl(it.key)}
		def labelsMetaData = new LabelsMetaDataImpl()
		labelNodes.each{ labelsMetaData.addRootNode( it ) }
		Instances vectorizedData = vectorizeData( loadStringTrainingData() )
		return new MultiLabelInstances( vectorizedData, labelsMetaData )
	}
	

	/* Convert string to word vector */
	public Instance makeInstance(String s){
		DenseInstance newStringInstance = new DenseInstance( dataTemplate.numAttributes() )
		Attribute docsAttr = dataTemplate.attribute("docs")
		println docsAttr
		newStringInstance.setValue( docsAttr, docsAttr.addStringValue( s ) )
		newStringInstance.setDataset( dataTemplate )
		filter.input( newStringInstance )
		return filter.output()
		
	}
	
	void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext
	}

}