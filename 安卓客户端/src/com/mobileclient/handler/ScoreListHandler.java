package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Score;
public class ScoreListHandler extends DefaultHandler {
	private List<Score> scoreList = null;
	private Score score;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (score != null) { 
            String valueString = new String(ch, start, length); 
            if ("scoreId".equals(tempString)) 
            	score.setScoreId(new Integer(valueString).intValue());
            else if ("studentObj".equals(tempString)) 
            	score.setStudentObj(valueString); 
            else if ("courseObj".equals(tempString)) 
            	score.setCourseObj(valueString); 
            else if ("courseScore".equals(tempString)) 
            	score.setCourseScore(new Float(valueString).floatValue());
            else if ("evaluate".equals(tempString)) 
            	score.setEvaluate(valueString); 
            else if ("addTime".equals(tempString)) 
            	score.setAddTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Score".equals(localName)&&score!=null){
			scoreList.add(score);
			score = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		scoreList = new ArrayList<Score>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Score".equals(localName)) {
            score = new Score(); 
        }
        tempString = localName; 
	}

	public List<Score> getScoreList() {
		return this.scoreList;
	}
}
