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
        try {
            Document doc = Jsoup.connect(url).get();
            Elements metaTags = doc.select("meta[property=og:image], meta[name=twitter:image], meta[itemprop=image]");

            for (Element metaTag : metaTags) {
                String imageUrl = metaTag.attr("content");

                if (!imageUrl.isEmpty()) {
                    if (metaTag.hasAttr("itemprop")) {
                        return url + "/" + imageUrl;
                    }
                    return imageUrl;
                }
            }
        } catch (IOException e) {
            throw new UrlNotFoundException(url);
        }

        return null;
    }

}
