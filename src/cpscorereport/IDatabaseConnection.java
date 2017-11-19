package cpscorereport;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public interface IDatabaseConnection {

	ArrayList<Team> loadList(String url) throws MalformedURLException, IOException;
	
}
