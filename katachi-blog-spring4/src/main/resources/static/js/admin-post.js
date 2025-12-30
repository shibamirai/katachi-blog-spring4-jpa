const deleteModal = document.getElementById('deleteModal')
if (deleteModal) {
	deleteModal.addEventListener('show.bs.modal', event => {
		// Button that triggered the modal
		const button = event.relatedTarget
		// Extract info from data-bs-* attributes
		const id = button.getAttribute('data-bs-id')
		const title = button.getAttribute('data-bs-title')
		// Update the modal's content.
		const modalMessage = deleteModal.querySelector('.modal-body p')
		const modalBodyInput = deleteModal.querySelector('.modal-footer input[name="id"]')

		modalMessage.textContent = `${title} を削除しますか？`
		modalBodyInput.value = id
	})
}
