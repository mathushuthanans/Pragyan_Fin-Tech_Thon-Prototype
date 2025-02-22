@WebServlet("/fraudData")
public class TestDataServlet extends HttpServlet {
    @Autowired
    private FraudService fraudService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        List<FraudData> fraudDataList = fraudService.getFraudData();
        String jsonData = new Gson().toJson(fraudDataList); 

        out.print(jsonData);
        out.flush();

        System.out.println("JSON sent to client: " + jsonData); 
    }
}