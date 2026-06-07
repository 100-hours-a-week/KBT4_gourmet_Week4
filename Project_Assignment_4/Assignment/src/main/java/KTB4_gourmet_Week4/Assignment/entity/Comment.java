/*
package KTB4_gourmet_Week4.Assignment.entity;

public class Comment {
}
*/

package KTB4_gourmet_Week4.Assignment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Comment {

    private Long id;
    private Long postId;
    private Long userId;
    private String content;

    public void update(String content) {
        this.content = content;
    }
}
