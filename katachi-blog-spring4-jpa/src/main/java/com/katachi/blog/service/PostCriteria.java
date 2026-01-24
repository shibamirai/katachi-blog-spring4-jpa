package com.katachi.blog.service;

import java.util.Optional;

import lombok.Value;

/**
 * Post の検索パラメータをまとめたクラス
 * @Value により Getter と、全フィールドを引数にもつコンストラクタを生成
 */
@Value
public class PostCriteria {

	Optional<String> titleOrBody;
	Optional<Integer> categoryId;
	Optional<Integer> userId;

}
