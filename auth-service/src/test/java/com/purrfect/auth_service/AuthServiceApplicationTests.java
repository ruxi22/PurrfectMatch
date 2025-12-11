@WebMvcTest(AuthController.class)
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void registerEndpointExists() throws Exception {
		mockMvc.perform(post("/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"test\",\"password\":\"pass\"}"))
				.andExpect(status().is4xxClientError());
	}
}
