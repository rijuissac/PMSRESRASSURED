package PMSRESTASSUREDTEST.PMSRESTASSUREDTEST;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import io.restassured.response.Response;
import junit.framework.Assert;

public class PMSEndToEnd
{

	static String pizzaId;
	static String message;


	public static void main(String[] args) throws InterruptedException
	{

		RestAssured.baseURI = "http://localhost:8080/";
		CREATE_CRUD();
		GET_CRUD();
		UPDATE_CRUD();
		Thread.sleep(5000);
		DELETE_CRUD();
		Thread.sleep(5000);

	}

	public static void DELETE_CRUD()
	{
		String response;

		response = given().header("Content-Type", "application/json").body(deletePizaaPayload())
				.when().delete("deletepizza").then().extract().body().asString();

		System.out.println(response);
		Assert.assertEquals("Pizza Record Deleted",response);	

	}

	public static String deletePizaaPayload()
	{
		return "{\r\n"
				+ "    \"id\" : \""+pizzaId+"\"\r\n"
				+ "}";
	}

	public static void UPDATE_CRUD()
	{
		String response;

		response = given().header("Content-Type", "application/json").body(updatePizzaPayload())
				.when().put("updatepizza/" + pizzaId).then().extract().asString();

		JsonPath jpath = new JsonPath(response);
		System.out.println(response);
		Assert.assertEquals("VeggieSupremePizza", jpath.getString("product"));
		Assert.assertEquals("750", jpath.getString("price"));
		Assert.assertEquals(pizzaId, jpath.getString("id"));

	}

	public static String updatePizzaPayload()
	{
		return "{\r\n" 
				+ "    \"product\" : \"VeggieSupremePizza\",\r\n" 
				+ "    \"price\": \"750\"\r\n" 
				+ "}";
	}

	public static void GET_CRUD()
	{
		String response;

		response = given().when().get("getpizza/" + pizzaId).then().extract().asString();
		
		System.out.println(response);
		
		JsonPath jpath = new JsonPath(response);

		Assert.assertEquals(pizzaId, jpath.getString("id"));
	}

	public static void CREATE_CRUD()
	{
		String response;

		response = given().header("Content-Type", "application/json").header("Connection", "keep-alive")
				.body(addPizzaPayload()).when().post("addpizza").then().extract().asString();
		System.out.println(response);
		JsonPath jpath = new JsonPath(response);
		pizzaId = jpath.getString("id");
		message = jpath.getString("msg");

		Assert.assertEquals("Success: Product is added", message);

	}

	public static String addPizzaPayload()
	{
		return "{\r\n" 
				+ "    \"product\" : \"VeggieSupreme1\",\r\n" 
				+ "    \"price\": \"500\"\r\n" 
				+ "}";
	}

}
