package redburning.github.io.npms.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import redburning.github.io.npms.utils.StringUtils;

@Document(indexName = "herbomics_index")
public class DocumentEntity {

	@Id
	private String id;
	
	private String number;
 
    private String mixedName;
 
    @Field(name = "chineseName")
    private String chineseName;
 
    private String ipt;
 
    private String englishName;
 
    private String synonyms;
 
    private String chineseNameAlias;
 
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
 
    private Double monoisotopicMass;
 
    private String molFile;
 
    private Double mPositiveHPositive;
 
    private Double mNegativeHNegative;

    private Double rtPositive;
 
    private Double pResponse;
 
    private Double positiveFrontInnerRef;
 
    private Double positiveBackInnerRef;
 
    private String positiveIonDataDesc;
 
    private Double rtNegative;
 
    private Double nResponse;
 
    private Double negativeFrontInnerRef;
 
    private Double negativeBackInnerRef;
 
    private String negativeIonDataDesc;
 
    private String smiles;
 
    private String inChIKey;
 
    private Double rt;
    
    private Double esiPositiveNegativeRatio;
    
    private String message;
    
    private transient List<String> stringFieldValues = new ArrayList<>();

    public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
    
	public String getMixedName() {
		return mixedName;
	}

	public void setMixedName(String mixedName) {
		this.mixedName = mixedName;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getIpt() {
		return ipt;
	}

	public void setIpt(String ipt) {
		this.ipt = ipt;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(String synonyms) {
		this.synonyms = synonyms;
	}

	public String getChineseNameAlias() {
		return chineseNameAlias;
	}

	public void setChineseNameAlias(String chineseNameAlias) {
		this.chineseNameAlias = chineseNameAlias;
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

	public void setPubChemCID(Double pubChemCID) {
		this.pubChemCID = String.valueOf(pubChemCID).replace(".0", "");
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

	public Double getMonoisotopicMass() {
		return monoisotopicMass;
	}

	public void setMonoisotopicMass(Double monoisotopicMass) {
		this.monoisotopicMass = monoisotopicMass;
	}

	public String getMolFile() {
		return molFile;
	}

	public void setMolFile(String molFile) {
		this.molFile = molFile;
	}

	public Double getmPositiveHPositive() {
		return mPositiveHPositive;
	}

	public void setmPositiveHPositive(Double mPositiveHPositive) {
		this.mPositiveHPositive = mPositiveHPositive;
	}

	public Double getmNegativeHNegative() {
		return mNegativeHNegative;
	}

	public void setmNegativeHNegative(Double mNegativeHNegative) {
		this.mNegativeHNegative = mNegativeHNegative;
	}

	public Double getRtPositive() {
		return rtPositive;
	}

	public void setRtPositive(Double rtPositive) {
		this.rtPositive = rtPositive;
	}

	public Double getpResponse() {
		return pResponse;
	}

	public void setpResponse(Double pResponse) {
		this.pResponse = pResponse;
	}

	public Double getPositiveFrontInnerRef() {
		return positiveFrontInnerRef;
	}

	public void setPositiveFrontInnerRef(Double positiveFrontInnerRef) {
		this.positiveFrontInnerRef = positiveFrontInnerRef;
	}

	public Double getPositiveBackInnerRef() {
		return positiveBackInnerRef;
	}

	public void setPositiveBackInnerRef(Double positiveBackInnerRef) {
		this.positiveBackInnerRef = positiveBackInnerRef;
	}

	public String getPositiveIonDataDesc() {
		return positiveIonDataDesc;
	}

	public void setPositiveIonDataDesc(String positiveIonDataDesc) {
		this.positiveIonDataDesc = positiveIonDataDesc;
	}

	public Double getRtNegative() {
		return rtNegative;
	}

	public void setRtNegative(Double rtNegative) {
		this.rtNegative = rtNegative;
	}

	public Double getnResponse() {
		return nResponse;
	}

	public void setnResponse(Double nResponse) {
		this.nResponse = nResponse;
	}

	public Double getNegativeFrontInnerRef() {
		return negativeFrontInnerRef;
	}

	public void setNegativeFrontInnerRef(Double negativeFrontInnerRef) {
		this.negativeFrontInnerRef = negativeFrontInnerRef;
	}

	public Double getNegativeBackInnerRef() {
		return negativeBackInnerRef;
	}

	public void setNegativeBackInnerRef(Double negativeBackInnerRef) {
		this.negativeBackInnerRef = negativeBackInnerRef;
	}

	public String getNegativeIonDataDesc() {
		return negativeIonDataDesc;
	}

	public void setNegativeIonDataDesc(String negativeIonDataDesc) {
		this.negativeIonDataDesc = negativeIonDataDesc;
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

	public Double getRt() {
		return rt;
	}

	public void setRt(Double rt) {
		this.rt = rt;
	}

	public Double getEsiPositiveNegativeRatio() {
		return esiPositiveNegativeRatio;
	}

	public void setEsiPositiveNegativeRatio(Double esiPositiveNegativeRatio) {
		this.esiPositiveNegativeRatio = esiPositiveNegativeRatio;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void addStringFieldValues(String value) {
		if (StringUtils.isNotEmpty(value)) {
			this.stringFieldValues.add(value);
		}
	}
	
	public List<String> getStringFieldValues() {
		return stringFieldValues;
	}
	
	@Override
	public String toString() {
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        return gson.toJson(this);
	}
}
