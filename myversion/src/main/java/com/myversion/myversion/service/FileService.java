// package com.myversion.myversion.service;

// public class FileService {

//     @Transactional
//     public byte[] getImageFile(Path path){
//         try {
//             byte[] imageBytes = Files.readAllBytes(path);
//             return imageBytes;
//         } catch (IOException e) {
//             throw new FileDownloadException("File download fail." + e.getMessage());
//         }
//     }

// }