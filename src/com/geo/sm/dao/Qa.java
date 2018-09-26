package com.geo.sm.dao;

import com.google.common.base.Objects;

public class Qa {

	private long id;

	private String question;

	private String answer;

	private CheckIn belongsTo;

	private QaType questionType;

	public CheckIn getBelongsTo() {
		return belongsTo;
	}

	public void setBelongsTo(CheckIn belongsTo) {
		this.belongsTo = belongsTo;
	}

	public QaType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QaType questionType) {
		this.questionType = questionType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(question, answer, belongsTo.getDateTime());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Qa) {
			Qa other = (Qa) obj;
			return Objects.equal(question, other.question)
					&& Objects.equal(answer, other.answer)
					&& Objects.equal(belongsTo.getDateTime(),
							other.belongsTo.getDateTime());
		} else {
			return false;
		}
	}
}
