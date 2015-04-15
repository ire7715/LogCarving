package ElasticLog;

import java.util.Calendar;
import java.util.Map;

import logCarvingBase.Log;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.json.JSONArray;
import org.json.JSONObject;

public class ElasticLog extends Log{
	
	private String host = null;
	private String cluster_name = null;
	private int apiport = 0;
	
	private String index = null;
	private String type = null;
	
	private Client client = null;

	@Override
	public void finalize() {
		this.client.close();
		this.client = null;
	}

	@Override
	public void close() {
		this.client.close();
		this.client = null;
	}
	
	protected boolean existedOrCreate(){
		boolean hasIndex = this.client.admin().indices().exists(new IndicesExistsRequest(this.index)).actionGet().isExists();
		if(!hasIndex){
			StringBuilder mappingXContent = new StringBuilder();
			mappingXContent.append("{");
				//mappingXContent.append("\"mappings\": {");
					mappingXContent.append("\"logging\": {");
						mappingXContent.append("\"_timestamp\": {");
							mappingXContent.append("\"enabled\":true, ");
							//mappingXContent.append("\"path\": \"sent_date\"");
							mappingXContent.append("\"store\": true");
						mappingXContent.append("}");
					mappingXContent.append("}");
				//mappingXContent.append("}");
			mappingXContent.append("}");
			
			/*XContentFactory.jsonBuilder()
				.startObject()
					.*/
				
			
			/*IndexRequest ir = new IndexRequest(this.index);
			ir.source(sb.toString());
			this.client.index(ir).actionGet();*/
			CreateIndexRequestBuilder cirBuilder = this.client.admin().indices().prepareCreate(this.index);
			cirBuilder.addMapping(this.type, mappingXContent.toString());
			cirBuilder.execute().actionGet();
		}
		return hasIndex;
	}

	@Override
	public void establishConnection(Map<String, String> info) throws ElasticsearchException {
		this.host = info.get("host");
		this.index = info.get("index");
		this.type = info.get("type");
		this.apiport = Integer.parseInt(info.get("apiport"));
		this.cluster_name = info.get("cluster_name");
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", this.cluster_name)
				.put("client.transport.ping_timeout","5s")
				.put("client.transport.nodes_sampler_interval","5s")
				.put("client.transport.sniff",false)
				.build();
		this.client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(this.host, this.apiport));
		
	}

	@Override
	public void send(int messageLabel, String message) throws ElasticsearchException {
		int i;
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		JSONObject excPack = new JSONObject();
		excPack.put("timestamp", Calendar.getInstance().getTime().getTime());
		excPack.put("level", messageLabel);
		excPack.put("type", "Custome message");
		excPack.put("message", message);
		JSONArray stackJSON = new JSONArray();
		for(StackTraceElement ele: stack)
			stackJSON.put(ele);
		excPack.put("stack", stackJSON);
		
		IndexRequest iR = new IndexRequest(this.index, this.type);
		iR.source(excPack.toString());
		this.client.index(iR).actionGet();
	}
	
	@Override
	public void send(int messageLabel, Exception e) throws ElasticsearchException {
		int i;
		StackTraceElement[] stack = e.getStackTrace();
		JSONObject excPack = new JSONObject();
		excPack.put("timestamp", Calendar.getInstance().getTime().getTime());
		excPack.put("level", messageLabel);
		excPack.put("type", e.toString());
		excPack.put("message", e.getMessage());
		JSONArray stackJSON = new JSONArray();
		for(StackTraceElement ele: stack)
			stackJSON.put(ele);
		excPack.put("stack", stackJSON);

		IndexRequest iR = new IndexRequest(this.index, this.type);
		iR.source(excPack.toString());
		client.index(iR).actionGet();
	}

}
