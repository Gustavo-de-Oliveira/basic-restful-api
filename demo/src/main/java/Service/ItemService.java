@Service
public class ItemService {
	
	private ItemFactory factory;
	private List<Item> itens;
	
	public void createItemFactory() {
		if(factory == null) {
		   factory = new TravelItemImpl();
		}
	}
	
	public void createItensList() {
		if(itens == null) {
		   itens = new ArrayList<>();
		}
	}
	
	public boolean isJSONValid(String jsonInString) {
	    try {
	       return new ObjectMapper().readTree(jsonInString) != null;
	    } catch (IOException e) {
	       return false;
	    }
	}
	
	private long parseId(JSONObject item) {
		return Long.valueOf((int) item.get("id"));
	}
	
	private void seItemValues(JSONObject jsonItem, Item item) {
		
		String nome = (String) jsonItem.get("nome");
		String link = (String) jsonItem.get("link");
	}

	public Item create(JSONObject jsonItem) {
		
		createFactory();
		
		Item item = factory.createItem();
		item.setId(parseId(jsonItem));
		seItemValues(jsonItem, item);
		
		return item;
	}

	public Item update(Item item, JSONObject jsonItem) {
		
		seItemValues(jsonItem, item);
		return item;
	}
	
	public void add(Item item) {
		createItensList();
		itens.add(item);
	}
	
    public List<Item> find() {
		createItensList();
		return itens;
	}
	
    public Item findById(long id) {
		return itens.stream().filter(t -> id == t.getId()).collect(Collectors.toList()).get(0);
	}
	
	public void delete() {
		itens.clear();
	}
	
	public void clearObjects() {
		itens = null;
		factory = null;
	}
}