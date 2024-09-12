package com.tosi.tale.taleDetail;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TaleDetailServiceImpl implements TaleDetailService {
    private static final String modelPath = "src/resources/ModelGenerator";
//    private final TaleDetailRepository taleDetailRepository;
//    private final S3Service s3Service;
//    private Map<String, String> nameMap;
//    private NameChanger nameChanger;
//
//    public TaleDto getTaleDetail(int taleId) {
//        Tale tale = taleDetailRepository.findById(taleId).orElseThrow(() -> new EntityNotFoundException());
//
//        String[] images = tale.getImages().split(",");
//        for (int i = 0; i < images.length; i++)
//            images[i] = s3Service.getPath(images[i]);
//
//        String total_contents = "";
//        total_contents += tale.getContent1();
//        total_contents += tale.getContent2();
//        total_contents += tale.getContent3();
//        if (tale.getContent4() != null)
//            total_contents += tale.getContent4();
//
//        String[] characters = tale.getCharacters().split(",");
//
//        TaleDto taleDto = TaleDto.builder()
//                .taleId(tale.getTaleId())
//                .title(tale.getTitle())
//                .content1(tale.getContent1())
//                .content2(tale.getContent2())
//                .content3(tale.getContent3())
//                .content4(tale.getContent4())
//                .total_contents(total_contents)
//                .images(images)
//                .thumbnail(images[0])
//                .characters(characters)
//                .time(tale.getTime())
//                .likeCnt(tale.getLikeCnt())
//                .build();
//
//        // 이름 담을 공간 초기화
//        nameMap = new LinkedHashMap<>();
//        for (String c : characters) {
//            nameMap.put(c, c);
//        }
//        return taleDto;
//
//    }
//
//    @Override
//    public void selectName(String CName, String BName) {
//        nameMap.put(CName, BName);
//    }
//
//    @Override
//    public String[] split_sentences(TaleDto taleDto) throws IOException {
//        String[] contents = new String[]{taleDto.getContent1(), taleDto.getContent2(), taleDto.getContent3(), taleDto.getContent4()};
//        String[] splitted_contents = new String[contents.length];
//        File[] files = new File[contents.length];
//
//        for (int i = 0; i < contents.length; i++) {
//            if (contents[i] == null) continue;
//
//            files[i] = File.createTempFile("content" + i, ".txt");
//
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(files[i]))) {
//                writer.write(contents[i]);
//            }
//
//            ProcessBuilder builder = new ProcessBuilder("node", "src/main/java/com/ssafy/tosi/taleDetail/morpheme/process.js", files[i].getAbsolutePath());
//            Process process = builder.start();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            StringBuilder response = new StringBuilder();
//            while ((line = reader.readLine()) != null) {
//                response.append(line + "\n");
//            }
//
//            splitted_contents[i] = response.toString();
//
//        }
//
//        return splitted_contents;
//
//    }
//
//    @Override
//    public String[] changeName(String[] contents) throws Exception {
//        nameChanger = new NameChanger();
//        String[] changedContents = new String[contents.length];
//
//        int c = 0;
//        for (String content : contents) {
//            if (content == null)
//                continue;
//
//            changedContents[c] = nameChanger.changeName(content, nameMap);
//            c++;
//        }
//
//        return changedContents;
//    }
//
//    @Override
//    public List<Page> paging(String[] changedContents, TaleDto taleDto) {
//        int p = 1;
//        List<Page> pages = new ArrayList<>();
//
//        for (int i = 0; i < changedContents.length; i++) {
//            if (changedContents[i] == null)
//                continue;
//
//            String[] sentences = changedContents[i].split("\n");
//
//            for (int j = 0; j < sentences.length - 1; j += 2) {
//                Page page = Page.builder()
//                        .leftNo(p)
//                        .left(taleDto.getImages()[i])
//                        .rightNo(p + 1)
//                        .right(sentences[j] + "\n" + sentences[j + 1])
//                        .flipped(false)
//                        .build();
//
//                pages.add(page);
//                p += 2;
//            }
//
//            // 문장이 홀수개 일 때
//            if (sentences.length % 2 != 0) {
//                Page page = Page.builder()
//                        .leftNo(p)
//                        .left(taleDto.getImages()[i])
//                        .rightNo(p + 1)
//                        .right(sentences[sentences.length - 1])
//                        .flipped(false)
//                        .build();
//
//                pages.add(page);
//            }
//
//        }
//
//        return pages;
//    }
//
//    @Override
//    public int updateLikeCnt(int taleId) {
//        return taleDetailRepository.updateLikeCnt(taleId);
//    }


}
