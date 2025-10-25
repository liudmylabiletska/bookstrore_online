package mate.academy.bookstoreonline.dto;

public record BookSearchParametersDto(String[] titles, String[] authors, String[] isbns) {
}
