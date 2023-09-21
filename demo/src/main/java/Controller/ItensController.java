@RestController
@RequestMapping("/basic-api/itens")
public class ItemController {
	
	private static final Logger logger = Logger.getLogger(ItemController.class);
	
	@Autowired
	private ItemService itemService;
	
	@GetMapping
	public ResponseEntity<List<Item>> find() {
		if(itemService.find().isEmpty()) {
			return ResponseEntity.notFound().build(); 
		}

		logger.info(itemService.find());
		return ResponseEntity.ok(itemService.find());
	}
	
	@DeleteMapping
	public ResponseEntity<Boolean> delete() {
		try {
			itemService.delete();
			return ResponseEntity.noContent().build();

		}catch(Exception e) {
			logger.error(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@PostMapping
	@ResponseBody
	public ResponseEntity<Item> create(@RequestBody JSONObject item) {
		try {
			if(itemService.isJSONValid(item.toString())) {
				Item itemCreated = itemService.create(item);
				var uri = ServletUriComponentsBuilder.fromCurrentRequest().path(itemCreated.getOrderNumber()).build().toUri();
				
				if(itemService.isStartDateGreaterThanEndDate(itemCreated)){
					logger.error("The start date is greater than end date.");
					return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);

				} else {
					itemService.add(itemCreated);
					return ResponseEntity.created(uri).body(null);
				}
			} else {
				return ResponseEntity.badRequest().body(null);
			}
		}catch(Exception e) {
			logger.error("JSON fields are not parsable. " + e);
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}
	
	@PutMapping(path = "/{id}", produces = { "application/json" })
	public ResponseEntity<Item> update(@PathVariable("id") long id, @RequestBody JSONObject item) {
		try {
			if(itemService.isJSONValid(item.toString())) {
				Item itemToUpdate = itemService.findById(id);
				if(itemToUpdate == null){
					logger.error("Item not found.");
					return ResponseEntity.notFound().build(); 
				} else {
					itemToUpdate = itemService.update(itemToUpdate, item);
					return ResponseEntity.ok(itemToUpdate);
				}
			}else {
				return ResponseEntity.badRequest().body(null);
			}
		}catch(Exception e) {
			logger.error("JSON fields are not parsable." + e);
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
	}
}