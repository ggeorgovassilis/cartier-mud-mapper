package cartier.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import cartier.model.Exit;
import cartier.model.Exit.Direction;
import cartier.model.Exit.Type;
import cartier.model.Map;
import cartier.model.Tile;

public class MapConverter {

	static int toint(Attribute a){
		if (a == null)
			return 0;
		return Integer.parseInt(a.getStringValue());
	}

	static String str(Attribute a){
		if (a == null)
			return "";
		return a.getStringValue();
	}
	
	static Exit nodeToExit(Element e){
		Exit exit = new Exit();
		exit.setDirection(Direction.valueOf(e.attributeValue("direction")));
		exit.setType(Type.valueOf(e.attributeValue("type")));
		exit.setName(e.attributeValue("name"));
		return exit;
	}

	public static Tile nodeToTile(Element node){
		Tile tile = new Tile();
		tile.setArrows(toint(node.attribute("arrows")));
		tile.setColumn(toint(node.attribute("column")));
		tile.setRow(toint(node.attribute("row")));
		tile.setLevel(toint(node.attribute("level")));
		tile.setText(str(node.attribute("text")));
		tile.setTitle(str(node.attribute("title")));
		for (Object tag:node.selectNodes("tag")){
			String tagValue = ((Element)tag).getText();
			tile.getTags().add(tagValue);
		}
		for (Object exit:node.selectNodes("exit")){
			Element eExit = (Element)exit;
			tile.getExits().add(nodeToExit(eExit));
		}
		return tile;
	}

	public static void main(String... args) throws Exception {
		args=new String[]{"personalization/laketown.xml","personalization/laketown.js"};
		ObjectMapper om = new ObjectMapper();
		SAXReader reader = new SAXReader();
        Document document = reader.read(new File(args[0]));

        File fout = new File(args[1]);

        List<Element> list = document.selectNodes( "//tile" );
        
        Map map = new Map();
		for (Element node:list) {
			Tile tile = nodeToTile(node);
			map.getTiles().add(tile);
		}
		
		FileWriter fw = new FileWriter(fout);
		fw.write(om.writeValueAsString(map));
		fw.flush();
		fw.close();
	}
}
