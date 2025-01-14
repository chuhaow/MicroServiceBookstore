function generateGuestId() {
    if (typeof crypto !== 'undefined' && crypto.randomUUID) {
        return `${crypto.randomUUID()}`;
    }

    // Fallback for environments without crypto.randomUUID
    return `${([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
        (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
    )}`;
}

function getGuestId() {
    let guestId = localStorage.getItem('guestId');
    if (!guestId) {
        guestId = generateGuestId();
        localStorage.setItem('guestId', guestId);
    }
    return guestId;
}
