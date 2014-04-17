package mq.openwire;

public class JsonBuilder {
	StringBuilder sb;
	
	JsonBuilder() {
		reset();
	}
	
	public JsonBuilder add(String a, String b) {
		sb.append("\""+a+"\":\""+b+"\",");
		return this;
	}
	
	public String toJson(){
		sb.deleteCharAt(sb.length()-1); //delete the last ','
		sb.append("}");
		String out = sb.toString();
		reset();
		return out;
	}
	
	public void reset() {
		sb = new StringBuilder();
		sb.append("{");
	}
	
}
