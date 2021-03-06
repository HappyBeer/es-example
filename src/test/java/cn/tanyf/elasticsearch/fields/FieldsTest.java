package cn.tanyf.elasticsearch.fields;

import cn.tanyf.elasticsearch.BaseTestCase;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsAction;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.io.Streams;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * 
 * 多字段，例如：一个字段对应多个分词器
 * 
 * @author tan.yf
 * @date 2017年5月27日 上午10:21:26
 */
public class FieldsTest extends BaseTestCase{
	private String INDEX_NAME="test_multi_field";
    private String TYPE_NAME="test_multi_field";
	@Test
	public void multiFieldMapping() throws Exception{
		AdminClient adminClient = client.admin();
        IndicesAdminClient adminClientIndices = adminClient.indices();
        IndicesExistsRequest re = new IndicesExistsRequestBuilder(adminClientIndices,IndicesExistsAction.INSTANCE,INDEX_NAME).request();
        boolean flag = adminClientIndices.exists(re).actionGet().isExists();
        if(flag){
            adminClientIndices.delete(new DeleteIndexRequest(INDEX_NAME)).get();
        }
        // 预定义一个索引
        adminClientIndices.prepareCreate(INDEX_NAME).get();
        InputStream is = Streams.class.getResourceAsStream("/mapping/fields/fields.json");
        String mapping = Streams.copyToString(new InputStreamReader(is));
        System.out.println(mapping);
        PutMappingRequest mappingRequest = Requests.putMappingRequest(INDEX_NAME).type(TYPE_NAME).source(mapping,XContentFactory.xContentType(mapping));
        adminClientIndices.putMapping(mappingRequest).actionGet();
	}
}
