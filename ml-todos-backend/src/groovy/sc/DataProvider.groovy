package sc

import org.springframework.web.context.*
import javax.servlet.*
import mulan.classifier.transformation.BinaryRelevance;
import mulan.data.*;
import weka.classifiers.functions.SMO;
import weka.core.DenseInstance
import weka.core.Instance
import weka.core.Instances
import weka.core.SelectedTag
import weka.core.SparseInstance;
import weka.filters.unsupervised.attribute.StringToWordVector
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Attribute;
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import weka.core.stemmers.LovinsStemmer


public class DataProvider implements ServletContextAware {

	StringToWordVector filter
	def dataTemplate 
	def servletContext
	def arffPath
	def xmlPath
	
	public DataProvider(){
		filter = new StringToWordVector()
		filter.setOutputWordCounts(false);
		filter.setWordsToKeep(1000);
		filter.setTFTransform(false);
		filter.setIDFTransform(false);
		filter.setNormalizeDocLength(new SelectedTag(0,StringToWordVector.TAGS_FILTER));
		filter.setLowerCaseTokens(true);
		filter.setUseStoplist(true);
		filter.setStopwords(null); // use default stopword list
		filter.setStemmer(new LovinsStemmer());
		filter.setMinTermFreq(1);
		filter.setAttributeNamePrefix("_");
		filter.setAttributeIndices("first");
	}
	
	private InputStream loadFile(def path){
		return servletContext.getResourceAsStream( path )
	}
	
	
	private Instances loadStringTrainingData(){
		ArffLoader arffLoader = new ArffLoader()
		arffLoader.setSource( loadFile(arffPath)  )
		return arffLoader.getDataSet()
	}

	
	Instances vectorizeData(Instances data){
		dataTemplate = data.stringFreeStructure()
		filter.setInputFormat( data )
		def vectorized = StringToWordVector.useFilter( data, filter )
		return vectorized
	}

	
	public List loadLabels(){
		InputStream stream = servletContext.getResourceAsStream(xmlPath)
    	def xmlLabels = new XmlParser().parse( stream )
    	return xmlLabels.label.collect{ new LabelNodeImpl( it.@name ) }
	}
	
	public MultiLabelInstances loadMultiLabelTrainingInstances(){
    	def labelNodes = loadLabels()
		def labelsMetaData = new LabelsMetaDataImpl()
		labelNodes.each{ labelsMetaData.addRootNode( it ) }
		Instances vectorizedData = vectorizeData( loadStringTrainingData() )
		return new MultiLabelInstances( vectorizedData, labelsMetaData )
	}
	
	
	/*
	public MultiLabelInstances loadMultiLabelTrainingInstances(){
		def labelNodes = Label.list().collect{new LabelNodeImpl(it.key)}
		def labelsMetaData = new LabelsMetaDataImpl()
		labelNodes.each{ labelsMetaData.addRootNode( it ) }
		Instances vectorizedData = vectorizeData( loadStringTrainingData() )
		return new MultiLabelInstances( vectorizedData, labelsMetaData )
	}*/

	/* Convert string to word vector */
	public Instance makeInstance(String s){
		DenseInstance newStringInstance = new DenseInstance( dataTemplate.numAttributes() )
		Attribute docsAttr = dataTemplate.attribute("docs")
		newStringInstance.setValue( docsAttr, docsAttr.addStringValue( s ) )
		newStringInstance.setDataset( dataTemplate )
		filter.input( newStringInstance )
		return filter.output()
		
	}
	
	void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext
	}

}