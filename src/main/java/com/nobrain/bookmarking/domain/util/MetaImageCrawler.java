package com.nobrain.bookmarking.domain.util;

import com.nobrain.bookmarking.domain.util.exception.UrlNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MetaImageCrawler {

    public String getMetaImageFromUrl(String url) {
        Document doc = null;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new UrlNotFoundException(url);
        }

        Elements metaTags = doc.getElementsByTag("meta");
        for (Element metaTag : metaTags) {
            String property = metaTag.attr("property");

            if (property.equals("og:image")) {
                return metaTag.attr("content");
            }
        }

        return null;
    }
}
