package com.lakatuna.test.starter.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Book")
public class Book implements Serializable {

  @JsonProperty(value = "id")
  @Id
  @GenericGenerator(name = "uuid_gen", strategy = "com.lakatuna.test.starter.utils.IdGenerator")
  @GeneratedValue(generator = "uuid_gen")
  private String id;

  @JsonProperty(value = "author")
  @Column(length = 50)
  private String author;

  @JsonProperty(value = "country")
  @Column(length = 50)
  private String country;

  @JsonProperty(value = "image_link")
  @Column(name = "image_link", length = 300)
  private String imageLink;

  @JsonProperty(value = "language")
  @Column(length = 20)
  private String language;

  @JsonProperty(value = "link")
  @Column(length = 300)
  private String link;

  @JsonProperty(value = "pages")
  private Integer pages;

  @JsonProperty(value = "title")
  private String title;

  @JsonProperty(value = "year")
  private Integer year;

  @JsonProperty(value = "active")
  private Boolean active;

  //GETTERS & SETTERS

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getImageLink() {
    return imageLink;
  }

  public void setImageLink(String imageLink) {
    this.imageLink = imageLink;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public Integer getPages() {
    return pages;
  }

  public void setPages(Integer pages) {
    this.pages = pages;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Book book = (Book) o;
    return id == book.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Book{" +
      "id=" + id +
      ", author='" + author + '\'' +
      ", country='" + country + '\'' +
      ", imageLink='" + imageLink + '\'' +
      ", language='" + language + '\'' +
      ", link='" + link + '\'' +
      ", pages=" + pages +
      ", title='" + title + '\'' +
      ", year=" + year + '\'' +
      ", active=" + active +
      '}';
  }

}
