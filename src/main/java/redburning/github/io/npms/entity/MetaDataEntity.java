package redburning.github.io.npms.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import redburning.github.io.npms.utils.StringUtils;

@Document(indexName = "herbomics_index")
public class MetaDataEntity extends BaseEntity {

    @Field(name = "chineseName")
    private String chineseName;
 
    private String englishName;
 
    private String englishSynonyms;
    
    private String chineseSynonyms;
 
    private String pathway;
 
    private String superclass;
 
    @SerializedName("class")
    @Field(name = "class")
    @JsonProperty("class")
    private String clazz;
 
    private String pubChemCID;
 
    private String lotus;
 
    private String wikidata;
 
    private String species1;
 
    private String species2;
 
    private String species3;
 
    private String cas;
 
    private String molecularFormula;
    
    private int charge;
 
    private Double monoisotopicMass;
 
    private Double avgMolecularWeight;
    
    private String smiles;
 
    private String inChIKey;
 
    private JSONObject suggest;
    
    private Double mPlusH;
    
	private Double mMinusH;
    
    private Double mPlusNH4;
    
    private Double mPlusNa;
    
    private Double mPlusAcetate;
    
    private Double mPlusCI;
    
    private String bioactivity;
    
    private String reference;
    
    private transient List<String> suggestFieldValues = new ArrayList<>();
    
	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getEnglishSynonyms() {
		return englishSynonyms;
	}

	public void setEnglishSynonyms(String englishSynonyms) {
		this.englishSynonyms = englishSynonyms;
	}

	public String getChineseSynonyms() {
		return chineseSynonyms;
	}

	public void setChineseSynonyms(String chineseSynonyms) {
		this.chineseSynonyms = chineseSynonyms;
	}

	public String getPathway() {
		return pathway;
	}

	public void setPathway(String pathway) {
		this.pathway = pathway;
	}

	public String getSuperclass() {
		return superclass;
	}

	public void setSuperclass(String superclass) {
		this.superclass = superclass;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getPubChemCID() {
		return pubChemCID;
	}

	public void setPubChemCID(String pubChemCID) {
		this.pubChemCID = pubChemCID;
	}

	public String getLotus() {
		return lotus;
	}

	public void setLotus(String lotus) {
		this.lotus = lotus;
	}

	public String getWikidata() {
		return wikidata;
	}

	public void setWikidata(String wikidata) {
		this.wikidata = wikidata;
	}

	public String getSpecies1() {
		return species1;
	}

	public void setSpecies1(String species1) {
		this.species1 = species1;
	}

	public String getSpecies2() {
		return species2;
	}

	public void setSpecies2(String species2) {
		this.species2 = species2;
	}

	public String getSpecies3() {
		return species3;
	}

	public void setSpecies3(String species3) {
		this.species3 = species3;
	}

	public String getCas() {
		return cas;
	}

	public void setCas(String cas) {
		this.cas = cas;
	}

	public String getMolecularFormula() {
		return molecularFormula;
	}

	public void setMolecularFormula(String molecularFormula) {
		this.molecularFormula = molecularFormula;
	}

	public int getCharge() {
		return charge;
	}
	
	public void setCharge(int charge) {
		this.charge = charge;
	}
	
	public Double getMonoisotopicMass() {
		return monoisotopicMass;
	}

	public void setMonoisotopicMass(Double monoisotopicMass) {
		this.monoisotopicMass = monoisotopicMass;
	}

	public Double getAvgMolecularWeight() {
		return this.avgMolecularWeight;
	}
	
	public void setAvgMolecularWeight(Double avgMolecularWeight) {
		this.avgMolecularWeight = avgMolecularWeight;
	}
	
	public String getSmiles() {
		return smiles;
	}

	public void setSmiles(String smiles) {
		this.smiles = smiles;
	}

	public String getInChIKey() {
		return inChIKey;
	}

	public void setInChIKey(String inChIKey) {
		this.inChIKey = inChIKey;
	}

	public JSONObject getSuggest() {
		return suggest;
	}
	
	public void setSuggest(JSONObject suggest) {
		this.suggest = suggest;
	}
	
	public void addSuggestFieldValues(String value) {
		if (StringUtils.isNotEmpty(value)) {
			this.suggestFieldValues.add(value);
		}
	}
	
	public List<String> getSuggestFieldValues() {
		return suggestFieldValues;
	}
	
	public Double getMPlusH() {
		return mPlusH;
	}

	public void setMPlusH(Double mPlusH) {
		this.mPlusH = mPlusH;
	}

	public Double getMMinusH() {
		return mMinusH;
	}

	public void setMMinusH(Double mMinusH) {
		this.mMinusH = mMinusH;
	}

	public Double getMPlusNH4() {
		return mPlusNH4;
	}

	public void setMPlusNH4(Double mPlusNH4) {
		this.mPlusNH4 = mPlusNH4;
	}

	public Double getMPlusNa() {
		return mPlusNa;
	}

	public void setMPlusNa(Double mPlusNa) {
		this.mPlusNa = mPlusNa;
	}

	public Double getMPlusAcetate() {
		return mPlusAcetate;
	}

	public void setMPlusAcetate(Double mPlusAcetate) {
		this.mPlusAcetate = mPlusAcetate;
	}

	public Double getMPlusCI() {
		return mPlusCI;
	}

	public void setMPlusCI(Double mPlusCI) {
		this.mPlusCI = mPlusCI;
	}
	
	public String getBioactivity() {
		return bioactivity;
	}

	public void setBioactivity(String bioactivity) {
		this.bioactivity = bioactivity;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
}
