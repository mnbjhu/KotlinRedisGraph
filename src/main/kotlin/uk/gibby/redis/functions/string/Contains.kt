package uk.gibby.redis.functions.string

import uk.gibby.redis.attributes.primative.StringAttribute

/**
 * Contains
 *
 * @property hayStack
 * @property needle
 * @constructor Create empty Contains
 */
class Contains(private val hayStack: StringAttribute, private val needle: String)