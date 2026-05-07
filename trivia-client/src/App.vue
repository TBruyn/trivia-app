<script setup>
import { ref, onMounted } from 'vue'

const question = ref(null)
const selectedAnswer = ref(null)
const answerWasCorrect = ref(null)
const wrongAnswers = ref([])

async function fetchQuestion() {
  selectedAnswer.value = null
  answerWasCorrect.value = null

  const response = await fetch('http://localhost:8080/questions')
  const data = await response.json()
  question.value = data
  console.log(question.value)
}

async function checkAnswer(questionId, answer) {
  selectedAnswer.value = answer
  const response = await fetch('http://localhost:8080/checkanswers', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      questionId: questionId,
      answer: answer
    })
  })
  const data = await response.json()

  if (data.correct) {
    answerWasCorrect.value = true
  } else {
    wrongAnswers.value.push(answer)
  }
}

onMounted(() => {
  fetchQuestion()
})

</script>

<template>
  <div class="app">
    <div class="card">
      <div v-if="question">
        <p class="question" v-html="question.question"></p>

        <div class="answers">
          <button v-for="answer in question.answers" :key="answer" @click="checkAnswer(question.id, answer)" :class="{
            correct: selectedAnswer === answer && answerWasCorrect === true,
            incorrect: wrongAnswers.includes(answer),
          }" :disabled="answerWasCorrect === true || wrongAnswers.includes(answer)">
            {{ answer }}
          </button>
        </div>

        <button class="next-button" v-if="answerWasCorrect" @click="fetchQuestion">
          Next Question
        </button>
      </div>

      <div v-else>
        <p>Loading...</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap');

.app {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;

  background: linear-gradient(135deg, #2b1055, #7597de);
  font-family: 'Inter', sans-serif;

  padding: 2rem;
}

.card {
  width: 100%;
  max-width: 700px;

  background: rgba(20, 20, 30, 0.85);
  backdrop-filter: blur(10px);

  border-radius: 20px;

  padding: 2rem;

  box-shadow:
    0 10px 40px rgba(0, 0, 0, 0.4),
    0 0 40px rgba(120, 100, 255, 0.15);

  color: white;
}

.question {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 2rem;
  line-height: 1.5;
}

.answers {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

button {
  padding: 1rem;

  border: none;
  border-radius: 12px;

  background: rgba(255, 255, 255, 0.08);
  color: white;

  font-size: 1rem;
  font-weight: 500;

  cursor: pointer;

  transition:
    transform 0.15s ease,
    background 0.2s ease,
    box-shadow 0.2s ease;
}

button:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.16);
  transform: translateY(-2px);

  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
}

button:disabled {
  cursor: default;
}

button.correct {
  background: #1f7a3a;
}

button.incorrect {
  background: #8a2424;
}

.next-button {
  margin-top: 2rem;
  width: 100%;

  background: linear-gradient(135deg, #8b5cf6, #6366f1);
}

.next-button:hover {
  background: linear-gradient(135deg, #9f67ff, #7583ff);
}
</style>
