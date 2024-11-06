package com.myversion.myversion.controller;




@RestController
public class CompareController {

    // @PostMapping("/compareUpload")
    // public ResponseEnitiy<?> compareUpload(@RequestParam("file") MultipartFile file) throws IOException {
    //     String json_location = "classpath:sim_result_20241003_210352.json";
    //     Resource resource = resourceLoader.getResource(json_location);

    //     String jsonData = new String(Files.readAllBytes(resuorce.getFile().toPath()));
    //     return ResponseEntity.ok();
    // }

    @GetMapping("/compareScore")
    public ResponseEntity<?> compareScore(@RequestParam String coverDir) throws IOException {
        String jsonData = "{\"userDir\":\"Hello, World!\"}";
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok()
            .headers(jsonHeaders)
            .body(jsonData);
    }

    @GetMapping("/compareImage")
    public ResponseEntity<?> compareImage(@RequestParam String imageFile) throws IOException {
        byte[] imageBytes = Files.readAllBytes(Paths.get("sim_wave_20241003_210352.png"));
        InputStreamResource imageResource = new InputStreamResource(new ByteArrayInputStream(imageBytes));

        HttpHeaders imageHeaders = new HttpHeaders();
        imageHeaders.setContentType(MediaType.IMAGE_PNG);

        return ResoponseEntity.ok()
            .headers(imageHeaders)
            .body(imageResource);
    }

    public CompareController(CoverSongService coverSongService) {
        this.coverSongService = coverSongService;
    }

    @PostMapping("/save")
    public CoverSong saveCoverSong(@RequestBody CoverSong coverSong) {
        return coverSongService.save(coverSong);
    }
    // findAllByUserId
    @GetMapping("/{userId}")
    public List<CoverSong> findAllByUserId(@PathVariable String userId) {
        return coverSongService.findAllByUserId(userId);
    }

    @PatchMapping("/{id}")
    public CoverSong updateCoverSong(@PathVariable Long id, @RequestBody CoverSong updatedFields) {
        return coverSongService.updateCoverSong(id, updatedFields);
    }



}
