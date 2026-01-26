/**
 * 記事削除確認モーダル
 */
const deleteModal = document.getElementById('deleteModal')
if (deleteModal) {
	deleteModal.addEventListener('show.bs.modal', event => {
		// Button that triggered the modal
		const button = event.relatedTarget
		// Extract info from data-bs-* attributes
		const id = button.getAttribute('data-bs-id')
		const message = button.getAttribute('data-bs-message')
		// Update the modal's content.
		const modalMessage = deleteModal.querySelector('.modal-body p')
		modalMessage.textContent = message
		const modalBodyInput = deleteModal.querySelector('.modal-footer input[name="id"]')
		modalBodyInput.value = id

	})
}
