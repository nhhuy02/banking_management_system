package com.ojt.klb.banking_notification_service.core;


import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;


@Schema(description = "Trạng thái chung cho đối tượng của toàn bộ hệ thống")

public enum Status {
    @Schema(description  = "Xóa")
    DELETED(-1),

    @Schema(description = "Đã ẩn")
    HIDDEN(-2),

    @Schema(description = "Từ chối")
    REFUSE(-3),

    @Schema(description = "Hết hiển thị")
    OFFSCREEN(-4),

    @Schema(description = "Đã khóa")
    LOCKED(-6),

    @Schema(description = "Tạm ngưng")
    PAUSE(-7),

    @Schema(description = "Chờ xác nhận")
    CONFIRMATION(-8),

    @Schema(description = "Không hoạt động")
    INACTIVE(0),

    @Schema(description = "Hoạt động")
    ACTIVE(1),

    @Schema(description = "Chờ duyệt")
    PENDING(2),

    @Schema(description = "Đang hiển thị")
    SHOWING(3),

    @Schema(description = "Chờ xác thực")
    CONFIRMING(4),

    @Schema(description = "Chưa hiển thị")
    NOTDISPLAYED(5),

    @Schema(description = "Gửi")
    SEND(6),

    @Schema(description = "Đã hủy")
    CANCEL(7),

    @Schema(description = "Đã trả")
    RETURNED(8),

    @Schema(description = "Đã duyệt")
    ACCEPTED(9),

    @Schema(description = "Chờ gửi")
    SENDING(10),

    @Schema(description = "Lỗi")
    ERROR(11),


    @Schema(description = "Kết thúc")
    CLOSE(13),

    @Schema(description = "Dự thảo")
    DRAFT(14)

    ;
    private int value;

    Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        try {
            Field field = this.getClass().getField(this.name());
            // Lấy trực tiếp annotation Schema
            Schema schema = field.getAnnotation(Schema.class);
            if (schema != null) {
                return schema.description();
            }
        } catch (NoSuchFieldException e) {
            System.out.println("Error when getting text of Status enum: " + e.getMessage());
        }
        return this.name();
    }


    public void setValue(int value) {
        this.value = value;
    }



    @Override
    public String toString() {
        return String.valueOf(value);
    }





    @Component
    public static class Converter extends EnumConverter<Status, Integer> {
        public Converter() {
            super(Status.class);
        }
    }
}
