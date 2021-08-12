package my.springcloud.common.exception;

/**
 * 존재하지 않는 리소스 익셉션
 */
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3996566010754010739L;

	public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

}
