Damn Good Privacy (DGP)
=======================

DGP is a private system designed for people who desire the highest
level of security when transferring messages over an unsecure channel.
DGP is not compatible with PGP or GPG because it works differently.

DGP uses Elliptic Curve Cryptography including ECDSA and ECDH, and
Serpent for message encryption.

Generating Key Pairs
--------------------

DGP can generate key pairs. The public key can be shared with anyone
over an unsecure channel. The private key should remain secret, because
if leaked then all previous conversations will be compromised.

The keys contain the following information:

# Several (currently 25) Diffie-Hellman keys.

# A DSA key for signing messages, as an extra level of defense.

Encrypting a Message
--------------------

You can encrypt a message using your Private key and someone else's
Public key.

What happens behind the scenes:

# A random DH key is selected from the receiver's Public Key.

# A random DH key is selected from the sender's Private Key.

# A Shared Secret is calculated using the DH keys.

# The Shared Secret is hashed using SHA-256.

# The message is encrypted using Serpent with the Hashed Secret.

# The Encrypted Message is signed using the sender's private DSA Key.

# The DH Key Indexes, DSA Signature, and Encrypted Message are included
in the Message Body, which can be transmitted securely.

Decrypting a Message
--------------------

You must have the sender's public key details before decrypting the message.

What happens behind the scenes:

# The message signature is verified.

# The DH key indexes are used to calculate the Shared Secret.

# The Hashed Shared Secret is used to decrypt the message.

Vulnerability
---------------

The biggest vulnerability with DGP (and other similar security systems)
is with public key sharing. A man-in-the-middle attack is always a
concern.

This is a scenario which outlines the vulnerability:

# Bob and Alice want to communicate using DGP.

# Alice gets Bob's real public key.

# Alice uses her own private key to encrypt the message like normal.

# Alice sends her public key along with the message.

# An attacker intercepts the message.

# The attacker generates a new key pair which impersonates Alice.

# The attacker, also having access to Bob's public key, encrypts a
different message.

# The attacker sends the different public key and message to Bob.

# Alice and Bob will have no idea that any of this happened until
Bob tries to reply to Alice, but by now it's too late because both
Alice's message and Bob's reply are known by the attacker.

Solution
--------

The best way to prevent this issue is to use a trusted third party to
verify the authenticity of the public keys before attempting transmission.

Another way is to share the public keys over a secure channel such as
SSL, SSH, or a VPN to prevent man-in-the-middle attacks. 

Another solution is to use a Pre-Shared Key, which is shared over a secure
channel, and include the PSK in the first transmitted message to
verify authenticity.

When this is not possible, it may be necessary to transmit a dummy message
first to verify that the keys have exchanged successfully. However even
this is not a perfect form of security.

Assuming that the public key exchange was successful, DGP is immune to
man-in-the-middle attacks.


